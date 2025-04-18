import Hero from "./Hero";

type Movie = {
  imdbId: string;
  title: string;
  poster: string;
  trailerLink: string;
  backdrops: string[];
};

type HomeProps = {
  movies: Movie[];
};

const Home = ({ movies }: HomeProps) => {
  return <Hero movies={movies} />;
};

export default Home;
