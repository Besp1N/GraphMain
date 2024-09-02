import { Link, useLocation } from "react-router-dom";
import classes from "./mainNav.module.css";
import  { FC, ReactNode } from "react";
interface INavItemProps {
  href: string;
  className?: string;
  children:  string | ReactNode | JSX.Element;
  onClick?: () => void;
}

const NavItem: FC<INavItemProps> = function ({
  href,
  className,
  children,
  onClick,
}) {
  const path = useLocation();
  const baseClass = className;
  return (
    <Link
      to={href}
      className={
        href === path.pathname ? `${baseClass} ${classes.active}` : baseClass
      }
      onClick={onClick}
    >
      {children}
    </Link>
  );
};
export default NavItem;
