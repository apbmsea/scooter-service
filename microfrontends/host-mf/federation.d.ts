declare module "authMF/App" {
  const AuthMF: React.ComponentType;
  export default AuthMF;
}
declare module "userMF/App" {
  const UserMF: React.ComponentType;
  export default UserMF;
}
declare module "headerMF/App" {
  const HeaderMF: React.ComponentType;
  export default HeaderMF;
}

declare module "sharedMF/event" {
  export const event: {
    emit: <T = unknown>(event: string, detail?: T) => void;
    on: <T = unknown>(
      event: string,
      callback: (detail?: T) => void
    ) => () => void;
  };
}