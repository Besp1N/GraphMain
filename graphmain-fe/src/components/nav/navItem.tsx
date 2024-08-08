import { Link, useLocation } from "react-router-dom";
import classes from "./mainNav.module.css";
interface INavItemProps {
  href: string;
  children: JSX.Element | string;
}
export default function NavItem({ href, children }: INavItemProps) {
  const path = useLocation();

  return (
    <Link to={href} className={href === path.pathname ? classes.active : ""}>
      {children}
    </Link>
  );
}
