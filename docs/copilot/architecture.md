# Architecture Rules

## Backend stack

- Java 21
- Spring Boot 3
- PostgreSQL

## Layering

- Controllers call services.
- Services call repositories.
- Controllers must not contain business logic.
- Repositories must not contain business rules.

## Error handling

- Use a shared global exception handler.
- Return the API error format defined in `api-errors.md`.

## Events and side effects

- Keep side effects in the service layer.
- If an important business action happens, consider audit logging.

## Example structure

```text
controller -> service -> repository
```

## Good example

```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody TransferRequest request) {
        transferService.transfer(request);
        return ResponseEntity.ok().build();
    }
}
```

```java
@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final AuditService auditService;

    public void transfer(TransferRequest request) {
        // business logic here
        auditService.log("TRANSFER_CREATED", request.getSourceAccount());
        transferRepository.save(map(request));
    }
}
```
