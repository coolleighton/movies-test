import axios from "axios";

// Create an axios instance with a base URL and credentials
const api = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

export default api;
