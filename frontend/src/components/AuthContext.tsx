import { createContext, useState, useEffect, ReactNode } from "react";
import api from "./Api";

interface AuthContextType {
  isAuthenticated: boolean;
  username: string | null;
  loading: boolean;
  login: (username: string) => void;
  logout: () => Promise<void>;
  checkAuthStatus: () => Promise<boolean>;
}

export const AuthContext = createContext<AuthContextType>({
  isAuthenticated: false,
  username: null,
  loading: true,
  login: () => {},
  logout: async () => {},
  checkAuthStatus: async () => false,
});

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider = ({ children }: AuthProviderProps) => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [username, setUsername] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);

  // Check if user is authenticated on initial load
  useEffect(() => {
    const checkAuth = async () => {
      await checkAuthStatus();
      setLoading(false);
    };

    checkAuth();
  }, []);

  // Function to check authentication status with the backend
  const checkAuthStatus = async (): Promise<boolean> => {
    try {
      const response = await api.get("/api/auth/user");
      if (response.data && response.data.username) {
        setIsAuthenticated(true);
        setUsername(response.data.username);
        return true;
      } else {
        setIsAuthenticated(false);
        setUsername(null);
        return false;
      }
    } catch (error) {
      console.error("Auth check failed:", error);
      setIsAuthenticated(false);
      setUsername(null);
      return false;
    }
  };

  const login = (username: string) => {
    setIsAuthenticated(true);
    setUsername(username);
  };

  const logout = async () => {
    try {
      await api.post("/api/auth/logout");
      setIsAuthenticated(false);
      setUsername(null);
    } catch (error) {
      console.error("Logout failed:", error);
    }
  };

  return (
    <AuthContext.Provider
      value={{
        isAuthenticated,
        username,
        loading,
        login,
        logout,
        checkAuthStatus,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export default AuthProvider;
