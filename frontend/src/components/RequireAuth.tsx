import { JSX, useContext } from "react";
import { AuthContext } from "./AuthContext";
import { Navigate } from "react-router-dom";

interface RequireAuthProps {
  children: JSX.Element;
}

const RequireAuth = ({ children }: RequireAuthProps) => {
  const { username, password } = useContext(AuthContext);

  if (!username || !password) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default RequireAuth;
