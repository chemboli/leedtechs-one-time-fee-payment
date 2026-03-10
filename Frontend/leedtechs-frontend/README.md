# LeedTechs Fee Payment — Angular Frontend

Angular 17 standalone-component frontend for the LeedTechs one-time fee payment backend.

## Project Structure

```
src/app/
├── models/
│   └── api.models.ts           # TypeScript interfaces matching backend DTOs
├── services/
│   ├── payment.service.ts      # HTTP calls to Spring Boot API
│   ├── toast.service.ts        # Signal-based toast notifications
│   └── format.service.ts       # Currency, date, incentive formatting
├── components/
│   ├── payment/                # POST /one-time-fee-payment
│   ├── lookup/                 # GET  /students/:studentNumber
│   ├── register/               # POST /students
│   └── shared/
│       ├── header/             # Nav tabs + logo
│       └── toast/              # Toast notifications
├── app.component.ts            # Root shell
├── app.routes.ts               # Routes
└── app.config.ts               # Providers
```

## Setup

```bash
npm install
ng serve
```

App runs on http://localhost:4200.  
Backend must be running on http://localhost:8080.

## API Base URL

To change the backend URL, edit `src/app/services/payment.service.ts`:

```ts
private readonly baseUrl = 'http://localhost:8080';
```
