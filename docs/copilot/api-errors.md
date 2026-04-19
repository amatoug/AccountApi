# API Error Standard

All REST APIs must return this JSON format:

```json
{
  "errorCode": "STRING",
  "message": "STRING",
  "requestId": "STRING"
}
```

## Rules

- `errorCode` must be stable and readable.
- `message` must be clear for developers and support teams.
- `requestId` must come from the incoming request context or generated tracing context.

## Examples

```json
{
  "errorCode": "USER_NOT_FOUND",
  "message": "User not found",
  "requestId": "8b5e1c4d-12ab-44e3-a9e8-6f8822a1f001"
}
```

```json
{
  "errorCode": "INVALID_TRANSFER_AMOUNT",
  "message": "Transfer amount must be greater than zero",
  "requestId": "9a0f23ac-c782-4d29-b5f1-7e66c245d002"
}
```

## Spring Boot guidance

- Prefer a global `@RestControllerAdvice`.
- Do not return raw `Map<String, String>` for business errors.
- Use one shared DTO such as `ApiError`.
