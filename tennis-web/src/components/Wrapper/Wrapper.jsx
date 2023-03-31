import React from 'react';
import { Col, Container, Row } from 'react-bootstrap';
import Menu from "../Menu/Menu";

const Wrapper = (props) => {
    const { children } = props;
    return (
        <>
            <Menu />
            <div class='container-fluid' >
                <Row>
                    <Col>
                        {children}
                    </Col>
                </Row>
            </div>
        </>
    );
}

export default Wrapper;

