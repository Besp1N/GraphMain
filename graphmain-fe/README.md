# GraphMain (Predictive Maintenance) React UI/Frontend Application

This is a React-based UI application for viewing data and notifications from a predictive maintenance system. The app uses Vite for fast development builds and a streamlined production setup.
Features

- View data related to devices
- Receive notifications for maintenance/critical alerts
- Responsive UI for mobile devices

# Disclaimer

This project was created during the summer internship in 2024 for GlobalLogic Inc.

# Requirements

    Node.js (v16+ recommended)
    npm (v8+ recommended)

# Getting Started

## 1. Clone the repository:

```bash
git clone git clone https://github.com/Besp1N/GraphMain
cd GraphMain/graphmain-fe
```

## 2. Install dependencies:

Using npm:

```bash
npm install
```

## 3. Running the development server:

To start the app in development mode with hot reloading:

```bash
npm run dev
```

The app will be available at http://localhost:5173 (or another port if 5173 is occupied).

## 4. Building for production:

To create an optimized build for production:

```bash
npm run build
```

The production build will be generated in the dist folder.

## 5. Previewing the production build:

To preview the production build locally:

```bash
npm run preview
```

This will serve the contents of the dist folder for a final check before deploying.
Customizing Vite Configuration

If you need to adjust the build or dev settings, you can edit the vite.config.js file.

## 6. General architecture

- This is a simple React Single Page App (SPA) using React-Router-Dom.
- All communication with the backend to display data and manage auth state is in src/http/fetch.ts
- Most important is the context for Auth and App (with WebSocket) in src/store
- The app is conected to the backend using a WebSocket for real-time notifications
- Styling is implemented using Material UI.

## 7. Solutions

- API calls to the backend are performed with fetchSafe() and addCredentials() helper functions.
- fetchSafe() doesn't throw errors - it may return them. This way strict error checking is enforced in development.
- Breadcrumbs for easier navigation in useBreadcrumbs hook in AppContext
- Flash messages in useFlash in FlashContext. Flashes are stored in a queue.
- Data fetching state may be managed by useFetchSafe() - it returns loading, error, data, and fetch, where fetch may be called any time to reload the data.
