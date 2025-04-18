import { NavLink } from "react-router-dom";
import "@fortawesome/fontawesome-free/css/all.min.css"; // You can also put this in main.jsx or App.tsx

const Header = () => {
  return (
    <header className="bg-gray-900 text-white shadow-md absolute z-50 w-full">
      <div className="container mx-auto px-4 flex items-center justify-between flex-wrap py-3">
        <NavLink
          to="/"
          className="flex items-center text-yellow-400 text-xl font-semibold"
        >
          <i className="fas fa-video-slash mr-2"></i>
          Gold
        </NavLink>

        <input id="nav-toggle" type="checkbox" className="hidden peer" />
        <label
          htmlFor="nav-toggle"
          className="cursor-pointer lg:hidden text-white"
        >
          <svg className="w-6 h-6 fill-current" viewBox="0 0 24 24">
            <path d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </label>

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
        </nav>
      </div>
    </header>
  );
};

export default Header;
