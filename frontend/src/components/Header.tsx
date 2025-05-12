import { NavLink } from "react-router-dom";
import { Dispatch, SetStateAction, useContext } from "react";
import { AuthContext } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import "@fortawesome/fontawesome-free/css/all.min.css";

type Movie = {
  imdbId: string;
  title: string;
  poster: string;
  trailerLink: string;
  backdrops: string[];
  reviews: { body: string }[];
};

type HeaderProps = {
  setMovies: Dispatch<SetStateAction<Movie[]>>;
  setMovie: Dispatch<SetStateAction<Movie[]>>;
  setReviews: Dispatch<SetStateAction<Movie[]>>;
};

const Header = ({ setMovies, setMovie, setReviews }: HeaderProps) => {
  const { username, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    setMovies([]);
    setMovie([]);
    setReviews([]);
    logout();
    navigate("/login");
  };

  return (
    <header className="bg-gray-900 text-white shadow-md absolute z-50 w-full">
      <div className="container mx-auto px-4 flex items-center justify-between flex-wrap py-3">
        {/* Logo */}
        <NavLink
          to="/"
          className="flex items-center text-yellow-400 text-xl font-semibold"
        >
          <i className="fas fa-video-slash mr-2"></i>
          Gold
        </NavLink>

        {/* Hamburger Toggle */}
        <input id="nav-toggle" type="checkbox" className="hidden peer" />
        <label
          htmlFor="nav-toggle"
          className="cursor-pointer lg:hidden text-white"
        >
          III
        </label>

        {/* Navigation */}
        <nav className="w-full lg:w-auto hidden lg:flex flex-col lg:flex-row lg:items-center gap-4 lg:gap-6 mt-4 lg:mt-0 peer-checked:flex">
          <NavLink
            to="/"
            className={({ isActive }) =>
              `text-white hover:text-yellow-400 transition ${
                isActive ? "text-yellow-400" : ""
              }`
            }
          >
            Home
          </NavLink>

          {!username && (
            <>
              <NavLink
                to="/login"
                className={({ isActive }) =>
                  `text-white hover:text-yellow-400 transition ${
                    isActive ? "text-yellow-400" : ""
                  }`
                }
              >
                Login
              </NavLink>
              <NavLink
                to="/register"
                className={({ isActive }) =>
                  `text-white hover:text-yellow-400 transition ${
                    isActive ? "text-yellow-400" : ""
                  }`
                }
              >
                Register
              </NavLink>
            </>
          )}

          {username && (
            <button
              onClick={handleLogout}
              className="text-white hover:text-red-400 transition cursor-pointer"
            >
              Logout
            </button>
          )}
        </nav>
      </div>
    </header>
  );
};

export default Header;
