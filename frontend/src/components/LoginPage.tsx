import { useState, useContext } from "react";
import { AuthContext } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import api from "./Api";

const Login = () => {
  const [usernameInput, setUsernameInput] = useState("");
  const [passwordInput, setPasswordInput] = useState("");
  const [error, setError] = useState("");
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    try {
      // Create form data for Spring Security form login
      const formData = new URLSearchParams();
      formData.append("username", usernameInput);
      formData.append("password", passwordInput);

      // Send as form data with proper content type for Spring Security
      const response = await api.post("/api/auth/login", formData, {
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
      });

      console.log("Login successful:", response.data);

      // Update auth context
      login(usernameInput);

      // Redirect to home
      navigate("/");
    } catch (err: any) {
      console.error("Login error:", err);
      setError(
        err.response?.data?.error ||
          "Login failed. Please check your credentials."
      );
    }
  };

  return (
    <div className="w-full h-screen flex flex-row justify-center items-center">
      <form
        onSubmit={handleSubmit}
        className="flex flex-col gap-4 p-4 w-[50%] bg-blue-950 rounded-3xl"
      >
        <h2 className="text-2xl">Login</h2>

        {error && (
          <div className="bg-red-500 p-2 rounded text-white">{error}</div>
        )}

        <input
          type="text"
          placeholder="Username"
          value={usernameInput}
          onChange={(e) => setUsernameInput(e.target.value)}
          className="p-2"
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={passwordInput}
          onChange={(e) => setPasswordInput(e.target.value)}
          className="p-2"
          required
        />
        <button
          type="submit"
          className="bg-blue-600 p-2 cursor-pointer hover:brightness-125 duration-150 rounded-xl"
        >
          Login
        </button>
      </form>
    </div>
  );
};

export default Login;
