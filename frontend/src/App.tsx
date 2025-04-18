import { useState, useEffect, useCallback } from "react";
import { Routes, Route } from "react-router-dom";
import Layout from "./components/Layout";
import Home from "./components/Home";
import Header from "./components/Header";
import Trailer from "./components/Trailer";
import Reviews from "./components/Reviews";
import NotFound from "./components/NotFound";

import axios from "axios";

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

  const getMovies = async () => {
    try {
      const response = await axios.get("/api/v1/movies");
      setMovies(response.data);
    } catch (err) {
      console.log(err);
    }
  };

  const getMovieData = useCallback(async (movieId: string) => {
    try {
      const response = await axios.get(`/api/v1/movies/${movieId}`);
      console.log(response.data);
      console.log(response.data.reviewIds);

      setMovie(response.data);
      setReviews(response.data.reviewIds);
    } catch (error) {
      console.error(error);
    }
  }, []);

  useEffect(() => {
    getMovies();
  }, []);

  return (
    <div className="App min-h-screen bg-gray-900 text-white">
      <Header />
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route path="/" element={<Home movies={movies} />} />
          <Route path="/Trailer/:ytTrailerId" element={<Trailer />} />
          <Route
            path="/Reviews/:movieId"
            element={
              <Reviews
                getMovieData={getMovieData}
                movie={movie}
                reviews={reviews}
                setReviews={setReviews}
              />
            }
          />
          <Route path="*" element={<NotFound />} />
        </Route>
      </Routes>
    </div>
  );
}

export default App;
