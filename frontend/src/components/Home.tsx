import { Dispatch, SetStateAction, useEffect } from "react";
import Hero from "./Hero";
import api from "./Api";

type Movie = {
  imdbId: string;
  title: string;
  poster: string;
  trailerLink: string;
  backdrops: string[];
  reviews: { body: string }[];
};

type HomeProps = {
  movies: Movie[];
  setMovies: Dispatch<SetStateAction<Movie[]>>;
};

const Home = ({ setMovies, movies }: HomeProps) => {
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

  return <Hero movies={movies} />;
};

export default Home;
