import { Link, useLocation } from "react-router-dom";
import classes from "./mainNav.module.css";
interface INavItemProps {
  href: string;
  className?: string;
  children: JSX.Element | string;
}
export default function NavItem({ href, className, children }: INavItemProps) {
  const path = useLocation();
  const baseClass = className;
  return (
    <Link
      to={href}
      className={
        href === path.pathname ? `${baseClass} ${classes.active}` : baseClass
      }
    >
      {children}
    </Link>
  );
}
