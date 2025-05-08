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

  if (movies.length > 1) {
    return <Hero movies={movies} />;
  } else
    return (
      <>
        <div className="w-full h-screen flex flex-row justify-center items-center">
          <p className="flex flex-col gap-4 px-4 py-10 text-2xl w-[50%] bg-blue-950 rounded-3xl text-center">
            Please login to view content
          </p>
        </div>
      </>
    );
};

export default Home;
