import { useParams } from "react-router-dom";
import ReactPlayer from "react-player";

const Trailer = () => {
  const { ytTrailerId } = useParams<{ ytTrailerId: string | undefined }>();

  return (
    <div className="w-full h-screen flex justify-center items-center pt-12 bg-black">
      {ytTrailerId ? (
        <ReactPlayer
          controls
          playing
          url={`https://www.youtube.com/watch?v=${ytTrailerId}`}
          width="100%"
          height="100%"
        />
      ) : null}
    </div>
  );
};

export default Trailer;
