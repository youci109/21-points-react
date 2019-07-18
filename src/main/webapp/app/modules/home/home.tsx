import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert, Progress, Button } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';
import { getUserWeeklyGoal, getEntities } from 'app/entities/preferences/preferences.reducer';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getLoginUrl } from 'app/shared/util/url-utils';
import PointsHome from 'app/entities/points/points-home';
import BloodPressureHome from 'app/entities/blood-pressure/blood-pressure-home';
import WeigthHome from 'app/entities/weigth/weigth-home';

export interface IHomeProp extends StateProps, DispatchProps {}

export class Home extends React.Component<IHomeProp> {
  componentDidMount() {
    this.props.getSession();
    if (this.props.account && this.props.account.login) {
      this.getUserWeeklyGoal();
      this.props.getEntities();
    }
  }

  componentDidUpdate(prevProps) {
    if (
      this.props.pointsThisWeek.length === 0 ||
      this.props.pointsThisWeek.points !== prevProps.pointsThisWeek.points ||
      this.props.account.login !== prevProps.account.login
    ) {
      this.getUserWeeklyGoal();
    }

    if (this.props.preferences.length === 0 || this.props.preferences.length !== prevProps.preferences.length) {
      this.props.getEntities();
    }
  }

  getUserWeeklyGoal = () => {
    this.props.getUserWeeklyGoal();
  };

  render() {
    const { account, pointsThisWeek, userWeeklyGoal, preferences } = this.props;
    return (
      <Row>
        <Col md="4" className="d-none d-md-inline">
          <span className="heart img-fluid rounded" />
        </Col>
        <Col md="8">
          {account && account.login ? (
            <div>
              <h2>
                <Translate contentKey="home.title" interpolate={{ firstName: account.firstName }}>
                  Welcome, {account.firstName}!
                </Translate>
              </h2>
            </div>
          ) : (
            <div>
              <h2>Welcome!</h2>
            </div>
          )}
          <p className="lead">
            <span>21-Points Health is here to track your health and improve your life. 😊</span>
          </p>
          {account && account.login ? (
            <div>
              <PointsHome pointsThisWeek={pointsThisWeek} userWeeklyGoal={userWeeklyGoal} />
              <BloodPressureHome />
              <WeigthHome />
              {preferences.length !== 0 && (
                <Row>
                  <Col md="12" className="mt-2">
                    <Button tag={Link} to={`entity/preferences/${preferences[0].id}/edit`} className="float-right" color="link" size="sm">
                      <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit Preferences</span>
                    </Button>
                  </Col>
                </Row>
              )}
              {account && <p>You are logged in as user {account.login}</p>}
            </div>
          ) : (
            <div>
              <p className="font-weight-bold">
                <span>To get started, please </span>
                <FontAwesomeIcon icon="sign-in-alt" />
                <a href={getLoginUrl()} className="alert-link">
                  <Translate contentKey="global.messages.info.authenticated.link">sign in</Translate>
                </a>
              </p>
              <div>
                Don't have an account yet?&nbsp;
                <Link to="/register" className="alert-link">
                  Register a new account
                </Link>
              </div>
            </div>
          )}
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
  pointsThisWeek: storeState.points.pointsThisWeek,
  userWeeklyGoal: storeState.preferences.userWeeklyGoal,
  preferences: storeState.preferences.entities
});

const mapDispatchToProps = {
  getSession,
  getUserWeeklyGoal,
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Home);
