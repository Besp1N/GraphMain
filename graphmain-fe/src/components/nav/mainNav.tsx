import classes from "./mainNav.module.css";
import NavItem from "./navItem";
export default function MainNav() {
  return (
    <nav className={classes["main-nav"]}>
      <ul>
        <li>
          <NavItem href={"/"}>Home</NavItem>
        </li>
        <li className="divider" />
        <li>
          <NavItem href={"/devices"}>Devices</NavItem>
        </li>
      </ul>
    </nav>
  );
}
