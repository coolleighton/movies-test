import { FormEvent, MutableRefObject } from "react";

type ReviewFormProps = {
  handleSubmit: (e: FormEvent) => void;
  revText: MutableRefObject<HTMLTextAreaElement | null>;
  labelText: string;
  defaultValue?: string;
};

const ReviewForm = ({
  handleSubmit,
  revText,
  labelText,
  defaultValue = "",
}: ReviewFormProps) => {
  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <label
          htmlFor="review-textarea"
          className="block text-sm font-medium text-white mb-1"
        >
          {labelText}
        </label>
        <textarea
          id="review-textarea"
          ref={revText}
          defaultValue={defaultValue}
          rows={3}
          className="w-full p-3 rounded-md bg-gray-800 text-white border border-gray-600 focus:outline-none focus:ring-2 focus:ring-cyan-500"
        />
      </div>
      <button
        type="submit"
        className="px-4 py-2 rounded-md border border-cyan-500 text-cyan-400 hover:bg-cyan-600 hover:text-white transition"
      >
        Submit
      </button>
    </form>
  );
};

export default ReviewForm;
