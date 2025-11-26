import React, { Suspense } from "react";
import LoadingWidget from "../widgets/LoadingWidget/ui/LoadingWidget";

const UserMFLazy = React.lazy(() =>
  import("userMF/App").catch(() => ({ default: () => null }))
);

const UserMF = () => {
  return (
    <Suspense fallback={<LoadingWidget active={true} />}>
      <UserMFLazy />
    </Suspense>
  );
};

export default UserMF;
