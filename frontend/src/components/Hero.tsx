import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "@fortawesome/fontawesome-free/css/all.min.css";

type Movie = {
  imdbId: string;
  title: string;
  poster: string;
  trailerLink: string;
  backdrops: string[];
};

type HeroProps = {
  movies: Movie[];
};

const Hero = ({ movies }: HeroProps) => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const navigate = useNavigate();

  const currentMovie = movies[currentIndex];

  const handleReviews = (id: string) => {
    navigate(`/Reviews/${id}`);
  };

  const goToPrev = () => {
    setCurrentIndex((prev) => (prev === 0 ? movies.length - 1 : prev - 1));
  };

  const goToNext = () => {
    setCurrentIndex((prev) => (prev === movies.length - 1 ? 0 : prev + 1));
  };

  return (
    <div className="relative w-full h-screen bg-black overflow-hidden">
      {currentMovie && (
        <div
          className="w-full h-full bg-cover bg-center relative transition-all duration-700"
          style={{ backgroundImage: `url(${currentMovie.backdrops[0]})` }}
        >
          <div className="absolute inset-0 bg-black/70" />

          <div className="relative z-10 flex flex-col lg:flex-row items-center gap-8 px-6 lg:px-24 py-12 h-full justify-center lg:justify-start">
            <img
              src={currentMovie.poster}
              alt={currentMovie.title}
              className="w-40 lg:w-64 rounded shadow-lg"
            />

            <div className="text-white text-center lg:text-left max-w-xl">
              <h2 className="text-4xl font-bold mb-4">{currentMovie.title}</h2>

              <div className="flex flex-col sm:flex-row gap-4 mt-6 justify-center lg:justify-start">
                <Link to={`/Trailer/${currentMovie.trailerLink.slice(-11)}`}>
                  <button className="flex items-center gap-2 text-white bg-red-600 hover:bg-red-700 px-4 py-2 rounded-md transition cursor-pointer">
                    <i className="fas fa-circle-play text-xl"></i>
                    Watch Trailer
                  </button>
                </Link>

                <button
                  onClick={() => handleReviews(currentMovie.imdbId)}
                  className="bg-cyan-500 hover:bg-cyan-600 text-white px-4 py-2 rounded-md transition cursor-pointer"
                >
                  Reviews
                </button>
              </div>
            </div>
          </div>

          <div className="absolute top-1/2 left-0 transform h-full -translate-y-1/2 z-20">
            <button
              onClick={goToPrev}
              className="text-white text-7xl px-4 h-full hover:bg-black/30 transition cursor-pointer"
            >
              ‹
            </button>
          </div>
          <div className="absolute top-1/2 right-0 transform h-full -translate-y-1/2 z-20">
            <button
              onClick={goToNext}
              className="text-white text-7xl px-4 h-full hover:bg-black/30 transition cursor-pointer"
            >
              ›
            </button>
          </div>

          <div className="absolute bottom-4 left-1/2 transform -translate-x-1/2 flex gap-2">
            {movies.map((_, idx) => (
              <button
                key={idx}
                onClick={() => setCurrentIndex(idx)}
                className={`w-3 h-3 rounded-full ${
                  idx === currentIndex ? "bg-white" : "bg-gray-500"
                }`}
              />
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default Hero;
