import { useEffect, useRef, FormEvent } from "react";
import { useParams } from "react-router-dom";
import ReviewForm from "./ReviewForm";
import api from "./Api";

type Review = {
  body: string;
};

type Movie = {
  poster: string;
};

type ReviewsProps = {
  getMovieData: (movieId: string) => void;
  movie: Movie | null;
  reviews: Review[];
  setReviews: (reviews: Review[]) => void;
};

const Reviews = ({
  getMovieData,
  movie,
  reviews,
  setReviews,
}: ReviewsProps) => {
  const revText = useRef<HTMLTextAreaElement | null>(null);
  const { movieId } = useParams();

  console.log(reviews);

  useEffect(() => {
    if (movieId) {
      getMovieData(movieId);
    }
  }, [getMovieData, movieId]);

  const addReview = async (e: FormEvent) => {
    e.preventDefault();

    const rev = revText.current;
    if (!rev || !movieId) return;

    try {
      await api.post("/api/v1/reviews", {
        reviewBody: rev.value,
        imdbId: movieId,
      });

      const updatedReviews = [...(reviews || []), { body: rev.value }];
      rev.value = "";
      setReviews(updatedReviews);
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="max-w-6xl mx-auto p-4 text-white">
      <h3 className="text-2xl font-semibold mb-4">Reviews</h3>

      <div className="flex flex-col lg:flex-row gap-8">
        <div className="flex-shrink-0 w-full lg:w-1/3">
          <img
            src={movie?.poster}
            alt="Movie Poster"
            className="w-full rounded shadow-md"
          />
        </div>

        <div className="w-full lg:w-2/3">
          <ReviewForm
            handleSubmit={addReview}
            revText={revText}
            labelText="Write a Review?"
          />

          <hr className="my-6 border-gray-600" />

          <div className="space-y-4">
            {(reviews || []).map((r, idx) => (
              <div key={idx}>
                <p className="text-gray-300">{r.body}</p>
                <hr className="mt-2 border-gray-600" />
              </div>
            ))}
          </div>
        </div>
      </div>

      <hr className="mt-10 border-gray-700" />
    </div>
  );
};

export default Reviews;
