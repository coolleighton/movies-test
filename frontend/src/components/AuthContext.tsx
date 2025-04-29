import { createContext, useState, ReactNode } from "react";

interface AuthContextType {
  username: string;
  password: string;
  login: (username: string, password: string) => void;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextType>({
  username: "",
  password: "",
  login: () => {},
  logout: () => {},
});

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const login = (newUsername: string, newPassword: string) => {
    setUsername(newUsername);
    setPassword(newPassword);
  };

  const logout = () => {
    setUsername("");
    setPassword("");
  };

  return (
    <AuthContext.Provider value={{ username, password, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
