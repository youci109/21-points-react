import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './weigth.reducer';
import { IWeigth } from 'app/shared/model/weigth.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IWeigthDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class WeigthDetail extends React.Component<IWeigthDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { weigthEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="twentyOnePointsReactApp.weigth.detail.title">Weigth</Translate> [<b>{weigthEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="timestamp">
                <Translate contentKey="twentyOnePointsReactApp.weigth.timestamp">Timestamp</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={weigthEntity.timestamp} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="weight">
                <Translate contentKey="twentyOnePointsReactApp.weigth.weight">Weight</Translate>
              </span>
            </dt>
            <dd>{weigthEntity.weight}</dd>
            <dt>
              <Translate contentKey="twentyOnePointsReactApp.weigth.user">User</Translate>
            </dt>
            <dd>{weigthEntity.userLogin ? weigthEntity.userLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/weigth" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/weigth/${weigthEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ weigth }: IRootState) => ({
  weigthEntity: weigth.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WeigthDetail);
