import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "./Api";

const Register = () => {
  const [usernameInput, setUsernameInput] = useState("");
  const [passwordInput, setPasswordInput] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    try {
      const response = await api.post("/api/auth/register", {
        username: usernameInput,
        password: passwordInput,
      });

      console.log("Registration successful:", response.data);
      alert("Registration successful! Please login.");
      navigate("/login"); // Redirect to login
    } catch (err: any) {
      console.error("Registration error:", err);
      setError(
        err.response?.data?.error ||
          "Registration failed. Please try a different username."
      );
    }
  };

  return (
    <div className="w-full h-screen flex flex-row justify-center items-center">
      <form
        onSubmit={handleSubmit}
        className="flex flex-col gap-4 p-4 w-[50%] bg-blue-950 rounded-3xl"
      >
        <h2 className="text-2xl">Register</h2>

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
          className="bg-green-600 p-2 cursor-pointer hover:brightness-125 duration-150 rounded-xl"
        >
          Register
        </button>
      </form>
    </div>
  );
};

export default Register;
