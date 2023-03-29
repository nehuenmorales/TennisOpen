import React from 'react';
import { Link } from 'react-router-dom';
import { Nav, Navbar } from 'react-bootstrap';

const Menu = () => {
    return (
        <>
            <Navbar collapseOnSelect expand="md" bg="dark" variant="dark">
                <div className="container">
                    <Navbar.Brand>Tennis Open</Navbar.Brand>
                    <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                    <Navbar.Collapse id="responsive-navbar-nav">
                        <Nav className="mr-auto">
                            <Nav.Link as={Link} to="/">Partido</Nav.Link>
                            <Nav.Link as={Link} to="/jugador">Jugador</Nav.Link>
                            <Nav.Link as={Link} to="/cancha">Cancha</Nav.Link>
                            <Nav.Link as={Link} to="/entrenador">Entrenador</Nav.Link>
                            <Nav.Link as={Link} to="/about">About</Nav.Link>
                        </Nav>
                    </Navbar.Collapse>
                </div>
            </Navbar>
        </>
    );
}

export default Menu;
