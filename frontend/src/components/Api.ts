import axios from "axios";
const apiUrl = import.meta.env.VITE_API_URL;

// Create an axios instance with a base URL and credentials
const api = axios.create({
  baseURL: apiUrl,
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

export default api;
