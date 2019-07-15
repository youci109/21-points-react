import './point.scss';

import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/shared/reducers/user-management';
import { getEntity, updateEntity, createEntity, reset } from './points.reducer';
import { IPoints } from 'app/shared/model/points.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPointsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPointsUpdateState {
  isNew: boolean;
  userId: string;
}

export class PointsUpdate extends React.Component<IPointsUpdateProps, IPointsUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getUsers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      values.excercise ? (values.excercise = 1) : (values.excercise = 0);
      values.meals ? (values.meals = 1) : (values.meals = 0);
      values.alcohol ? (values.alcohol = 1) : (values.alcohol = 0);
      const { pointsEntity } = this.props;
      const entity = {
        ...pointsEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/points');
  };

  render() {
    const { pointsEntity, users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="twentyOnePointsReactApp.points.home.createOrEditLabel">
              <Translate contentKey="twentyOnePointsReactApp.points.home.createOrEditLabel">Create or edit a Points</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : pointsEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="points-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="points-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="dateLabel" for="points-date">
                    <Translate contentKey="twentyOnePointsReactApp.points.date">Date</Translate>
                  </Label>
                  <AvField
                    id="points-date"
                    type="date"
                    className="form-control"
                    name="date"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <span className="checkboxspan">
                    <AvInput
                      id="points-excercise"
                      type="checkbox"
                      name="excercise"
                      value={pointsEntity.excercise === 1 && !isNew}
                      className="checkboxoption"
                    />
                    <Label check for="excercise" className="checkboxLable">
                      <Translate contentKey="twentyOnePointsReactApp.points.excercise">Excercise</Translate>
                    </Label>
                  </span>
                  <span className="checkboxspan">
                    <AvInput
                      id="points-meals"
                      type="checkbox"
                      name="meals"
                      value={pointsEntity.meals === 1 && !isNew}
                      className="checkboxoption"
                    />
                    <Label check for="meals" className="checkboxLable">
                      <Translate contentKey="twentyOnePointsReactApp.points.meals">Meals</Translate>
                    </Label>
                  </span>
                  <span className="checkboxspan">
                    <AvInput
                      id="points-alcohol"
                      type="checkbox"
                      name="alcohol"
                      value={pointsEntity.alcohol === 1 && !isNew}
                      className="checkboxoption"
                    />
                    <Label check for="alcohol" className="checkboxLable">
                      <Translate contentKey="twentyOnePointsReactApp.points.alcohol">Alcohol</Translate>
                    </Label>
                  </span>
                </AvGroup>
                <AvGroup>
                  <Label id="notesLabel" for="points-notes">
                    <Translate contentKey="twentyOnePointsReactApp.points.notes">Notes</Translate>
                  </Label>
                  <AvField
                    id="points-notes"
                    type="text"
                    name="notes"
                    validate={{
                      maxLength: { value: 140, errorMessage: translate('entity.validation.maxlength', { max: 140 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="points-user">
                    <Translate contentKey="twentyOnePointsReactApp.points.user">User</Translate>
                  </Label>
                  <AvInput id="points-user" type="select" className="form-control" name="userId" required>
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.login}
                          </option>
                        ))
                      : null}
                  </AvInput>
                  <AvFeedback>
                    <Translate contentKey="entity.validation.required">This field is required.</Translate>
                  </AvFeedback>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/points" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  pointsEntity: storeState.points.entity,
  loading: storeState.points.loading,
  updating: storeState.points.updating,
  updateSuccess: storeState.points.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PointsUpdate);
