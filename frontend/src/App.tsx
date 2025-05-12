import { useState, useCallback, useEffect } from "react";
import { Routes, Route } from "react-router-dom";
import Layout from "./components/Layout";
import Home from "./components/Home";
import Header from "./components/Header";
import Trailer from "./components/Trailer";
import Reviews from "./components/Reviews";
import NotFound from "./components/NotFound";
import RequireAuth from "./components/RequireAuth";
import Login from "./components/LoginPage";
import Register from "./components/RegisterPage";
import api from "./components/Api";
import { AuthProvider } from "./components/AuthContext";

type Movie = {
  imdbId: string;
  title: string;
  poster: string;
  trailerLink: string;
  reviews: { body: string }[];
  backdrops: string[];
};

function App() {
  const [movies, setMovies] = useState<Movie[]>([]);
  const [movie, setMovie] = useState<Movie | null>(null);
  const [reviews, setReviews] = useState<{ body: string }[]>([]);

  // Fetch movies
  const getMovies = async () => {
    try {
      const response = await api.get("/api/v1/movies");
      setMovies(response.data);
      console.log(response.data);
    } catch (err) {
      console.error("API error:", err);
    }
  };

  // UseEffect to fetch movies on initial load
  useEffect(() => {
    getMovies();
  }, []);

  // Fetch movie data for specific movie
  const getMovieData = useCallback(async (movieId: string) => {
    try {
      const response = await api.get(`/api/v1/movies/${movieId}`);
      console.log(response.data);
      setMovie(response.data);
      setReviews(response.data.reviewIds);
    } catch (error) {
      console.error(error);
    }
  }, []);

  return (
    <AuthProvider>
      <div className="App min-h-screen bg-gray-900 text-white">
        <Header
          setMovies={setMovies}
          setMovie={setMovie}
          setReviews={setReviews}
        />
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route
              path="/"
              element={<Home movies={movies} setMovies={setMovies} />}
            />
            <Route path="/Trailer/:ytTrailerId" element={<Trailer />} />
            <Route
              path="/Reviews/:movieId"
              element={
                <RequireAuth>
                  <Reviews
                    getMovieData={getMovieData}
                    movie={movie}
                    reviews={reviews}
                    setReviews={setReviews}
                  />
                </RequireAuth>
              }
            />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="*" element={<NotFound />} />
          </Route>
        </Routes>
      </div>
    </AuthProvider>
  );
}

export default App;
