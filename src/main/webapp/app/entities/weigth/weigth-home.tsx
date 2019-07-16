import React, { Component } from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert, Progress } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

class WeigthHome extends Component {
  render() {
    return (
      <div>
        <Row className="mt-4">
          <Col xs="6" className="text-nowrap">
            <h4 className="mt-1">Weight:</h4>
          </Col>
          <Col xs="6" className="text-right">
            <Link to={`/`} className="btn btn-outline-secondary btn-sm">
              <FontAwesomeIcon icon="plus" />
              &nbsp;Weight
            </Link>
          </Col>
        </Row>
        <Row className="mt-1">
          <Col xs="12" md="11">
            Graph
          </Col>
        </Row>
      </div>
    );
  }
}

export default WeigthHome;
