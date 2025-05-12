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

// Add this to your api.tsx
api.interceptors.request.use(
  (config) => {
    console.log("Request config:", config);
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

api.interceptors.response.use(
  (response) => {
    console.log("Response:", response);
    return response;
  },
  (error) => {
    console.error("Response error:", error);
    return Promise.reject(error);
  }
);

export default api;
