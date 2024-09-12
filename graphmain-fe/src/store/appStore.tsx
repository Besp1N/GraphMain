import React, {
  createContext,
  useContext,
  useState,
  useEffect,
  ReactNode,
} from "react";
import { Client, Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { NotificationEntityQueryReturnType } from "../http/fetch";
import type { Option } from "../http/fetch";
const WEBSOCKET_URL = "http://localhost:8080/ws";
const QUEUE_CAPACITY = 10; // Fixed capacity for the message queue

// Define the type for the breadcrumbs state
interface AppContextType {
  breadcrumbs: [string, string][];
  setBreadcrumbs: (breadcrumbs: [string, string][]) => void;
  messageQueue: NotificationEntityQueryReturnType[];
  setMessageQueue: (queue: NotificationEntityQueryReturnType[]) => void;
  connectWebSocket: (token: string) => void;
  disconnectWebSocket: () => void;
  stompClient: Option<Client>;
}

// Create the context with a default value
export const AppContext = createContext<AppContextType | undefined>(undefined);

// Provider component
export const AppContextProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [breadcrumbs, setBreadcrumbs] = useState<[string, string][]>([]);
  const [messageQueue, setMessageQueue] = useState<
    NotificationEntityQueryReturnType[]
  >([]);
  const [stompClient, setStompClient] = useState<Option<Client>>();

  // Function to handle incoming messages
  const onMessageReceived = (message: unknown) => {
    if (
      typeof message === "object" &&
      message != null &&
      // typescript sucks
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      //@ts-expect-error
      typeof message?.body === "string"
    ) {
      let body: NotificationEntityQueryReturnType;
      try {
        // stupid typescript, already checked for it
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        //@ts-expect-error
        body = JSON.parse(message.body);
      } catch {
        console.error("Cannot deserialize the WebsocketMessage.");
        return;
      }

      setMessageQueue((mq) => {
        const newMessageQueue = [...mq, body];
        if (newMessageQueue.length > QUEUE_CAPACITY) {
          newMessageQueue.shift(); // Remove the oldest message if capacity exceeded
        }
        return newMessageQueue;
      });
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
      setStompClient(undefined);
      console.error("WebSocket Error:", error);
    };
    client.onDisconnect = () => {
      setStompClient(undefined);
    };
    client.onWebSocketClose = () => {
      setStompClient(undefined);
    };
    client.activate();
    setStompClient(client);
  };

  // Disconnect from the WebSocket
  const disconnectWebSocket = () => {
    if (stompClient) {
      stompClient.deactivate();
      setStompClient(undefined);
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
        setMessageQueue,
        stompClient,
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
