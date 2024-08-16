import { createContext, useContext, useState, ReactNode } from "react";
import { v4 as uuidv4 } from "uuid";

export type FlashType = "info" | "success" | "warning" | "error";

export interface Flash {
  id: string;
  type: FlashType;
  message: string;
}
interface FlashContextProps {
  flashes: Flash[];
  addFlash: (type: Flash["type"], message: string) => void;
  removeFlash: (id: string) => void;
}

const FlashContext = createContext<FlashContextProps | undefined>(undefined);

export const useFlash = () => {
  const context = useContext(FlashContext);
  if (!context) {
    throw new Error("useFlash must be used within a FlashProvider");
  }
  return context;
};

export const FlashProvider = ({ children }: { children: ReactNode }) => {
  const [flashes, setFlashes] = useState<Flash[]>([]);

  const addFlash = (type: Flash["type"], message: string) => {
    const newFlash: Flash = {
      id: uuidv4(),
      type,
      message,
    };
    setFlashes((prevFlashes) => [...prevFlashes, newFlash]);
  };

  const removeFlash = (id: string) => {
    setFlashes((prevFlashes) => prevFlashes.filter((flash) => flash.id !== id));
  };

  return (
    <FlashContext.Provider value={{ flashes, addFlash, removeFlash }}>
      {children}
    </FlashContext.Provider>
  );
};
