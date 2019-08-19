import React from 'react';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import Logs from './logs/logs';
import Health from './health/health';
import Metrics from './metrics/metrics';
import Configuration from './configuration/configuration';
import Audits from './audits/audits';
import Docs from './docs/docs';
import PointRank from './pointsRank/pointsRank';

const Routes = ({ match }) => (
  <div>
    <ErrorBoundaryRoute exact path={`${match.url}/health`} component={Health} />
    <ErrorBoundaryRoute exact path={`${match.url}/metrics`} component={Metrics} />
    <ErrorBoundaryRoute exact path={`${match.url}/docs`} component={Docs} />
    <ErrorBoundaryRoute exact path={`${match.url}/configuration`} component={Configuration} />
    <ErrorBoundaryRoute exact path={`${match.url}/audits`} component={Audits} />
    <ErrorBoundaryRoute exact path={`${match.url}/logs`} component={Logs} />
    <ErrorBoundaryRoute exact path={`${match.url}/rank`} component={PointRank} />
  </div>
);

export default Routes;
