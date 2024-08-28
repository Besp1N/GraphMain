// BreadcrumbsContext.tsx
import React, { createContext, useContext, useState, ReactNode } from "react";

// Define the type for the breadcrumbs state
interface AppContextType {
  breadcrumbs: [string, string][];
  setBreadcrumbs: (breadcrumbs: [string, string][]) => void;
}

// Create the context with a default value
const AppContext = createContext<AppContextType | undefined>(undefined);

// Provider component
export const AppContextProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [breadcrumbs, setBreadcrumbs] = useState<[string, string][]>([]);

  return (
    <AppContext.Provider value={{ breadcrumbs, setBreadcrumbs }}>
      {children}
    </AppContext.Provider>
  );
};

// Custom hook for easier access to the context
export const useBreadcrumbs = (): [
  [string, string][],
  (breadcrumbs: [string, string][]) => void
] => {
  const context = useContext(AppContext);
  if (!context) {
    throw new Error("Attempted to use breadcrumbs outside app context.");
  }
  const { breadcrumbs, setBreadcrumbs } = context;
  return [breadcrumbs, setBreadcrumbs];
};
