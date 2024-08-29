import React, {
  createContext,
  useContext,
  useState,
  useEffect,
  ReactNode,
} from "react";
import { Client, Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";
const WEBSOCKET_URL = "http://localhost:8080/ws";
const QUEUE_CAPACITY = 10; // Fixed capacity for the message queue

// Define the type for the breadcrumbs state
interface AppContextType {
  breadcrumbs: [string, string][];
  setBreadcrumbs: (breadcrumbs: [string, string][]) => void;
  messageQueue: string[];
  connectWebSocket: (token: string) => void;
  disconnectWebSocket: () => void;
}

// Create the context with a default value
export const AppContext = createContext<AppContextType | undefined>(undefined);

// Provider component
export const AppContextProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [breadcrumbs, setBreadcrumbs] = useState<[string, string][]>([]);
  const [messageQueue, setMessageQueue] = useState<string[]>([]);
  const [stompClient, setStompClient] = useState<Client | null>(null);

  // Function to handle incoming messages
  const onMessageReceived = (message: unknown) => {
    console.log(message);
    console.log(typeof message);
    if (message && message?.body) {
      const newMessageQueue = [...messageQueue, message.body];
      if (newMessageQueue.length > QUEUE_CAPACITY) {
        newMessageQueue.shift(); // Remove the oldest message if capacity exceeded
      }
      setMessageQueue(newMessageQueue);
    }
  };

  // Connect to the WebSocket
  const connectWebSocket = (token: string) => {
    const client = Stomp.over(() => SockJS(`${WEBSOCKET_URL}/?token=${token}`));

    client.onConnect = () => {
      console.log("Connected to WebSocket");
      client.subscribe("/notifications", onMessageReceived);
    };

    client.onStompError = (error) => {
      console.error("WebSocket Error:", error);
    };

    client.activate();
    setStompClient(client);
  };

  // Disconnect from the WebSocket
  const disconnectWebSocket = () => {
    if (stompClient) {
      stompClient.deactivate();
      setStompClient(null);
      console.log("Disconnected from WebSocket");
    }
  };

  useEffect(() => {
    // Clean up the WebSocket connection when the component unmounts
    return () => {
      disconnectWebSocket();
    };
  }, [stompClient]);

  return (
    <AppContext.Provider
      value={{
        breadcrumbs,
        setBreadcrumbs,
        messageQueue,
        connectWebSocket,
        disconnectWebSocket,
      }}
    >
      {children}
    </AppContext.Provider>
  );
};

// Custom hook for easier access to the breadcrumbs
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
