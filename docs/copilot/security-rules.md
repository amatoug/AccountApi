# Security Rules

All sensitive endpoints must follow these rules.

## Authentication and authorization

- Sensitive endpoints must use `@PreAuthorize`.
- Access rules must be explicit.
- Do not leave financial or admin endpoints open.

## Input validation

- Validate request bodies with Jakarta Validation annotations.
- Reject invalid input early.
- Never trust client-side validation alone.

## Secrets and sensitive data

- Never log passwords, tokens, API keys, or full card details.
- Never hardcode secrets in source code.
- Use environment variables or a secret manager.

## Audit logging

The following actions must create an audit log:
- money transfer
- password reset
- role change
- account lock or unlock

## Example

```java
@PreAuthorize("hasRole('ADMIN')")
@PostMapping("/transfer")
public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest request) {
    auditService.log("TRANSFER_REQUEST", request.getSourceAccount());
    transferService.transfer(request);
    return ResponseEntity.ok().build();
}
```
