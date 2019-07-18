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
import { getEntity, updateEntity, createEntity, reset } from './blood-pressure.reducer';
import { IBloodPressure } from 'app/shared/model/blood-pressure.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import moment from 'moment';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

export interface IBloodPressureUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IBloodPressureUpdateState {
  isNew: boolean;
  userId: string;
}

export class BloodPressureUpdate extends React.Component<IBloodPressureUpdateProps, IBloodPressureUpdateState> {
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
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getUsers();
  }

  saveEntity = (event, errors, values) => {
    values.timestamp = convertDateTimeToServer(values.timestamp);

    if (errors.length === 0) {
      const { bloodPressureEntity } = this.props;
      const entity = {
        ...bloodPressureEntity,
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
    this.props.history.goBack();
  };

  render() {
    const { bloodPressureEntity, users, loading, updating, isAdmin } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="twentyOnePointsReactApp.bloodPressure.home.createOrEditLabel">
              <Translate contentKey="twentyOnePointsReactApp.bloodPressure.home.createOrEditLabel">
                Create or edit a BloodPressure
              </Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : bloodPressureEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="blood-pressure-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="blood-pressure-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="timestampLabel" for="blood-pressure-timestamp">
                    <Translate contentKey="twentyOnePointsReactApp.bloodPressure.timestamp">Timestamp</Translate>
                  </Label>
                  <AvInput
                    id="blood-pressure-timestamp"
                    type="datetime-local"
                    className="form-control"
                    name="timestamp"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={
                      isNew ? convertDateTimeFromServer(moment.now()) : convertDateTimeFromServer(this.props.bloodPressureEntity.timestamp)
                    }
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="systolicLabel" for="blood-pressure-systolic">
                    <Translate contentKey="twentyOnePointsReactApp.bloodPressure.systolic">Systolic</Translate>
                  </Label>
                  <AvField
                    id="blood-pressure-systolic"
                    type="string"
                    className="form-control"
                    name="systolic"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="diastolicLabel" for="blood-pressure-diastolic">
                    <Translate contentKey="twentyOnePointsReactApp.bloodPressure.diastolic">Diastolic</Translate>
                  </Label>
                  <AvField
                    id="blood-pressure-diastolic"
                    type="string"
                    className="form-control"
                    name="diastolic"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                {isAdmin && (
                  <AvGroup>
                    <Label for="user.login">User</Label>
                    <AvInput id="blood-pressure-user" type="select" className="form-control" name="userId">
                      {users
                        ? users.map(otherEntity => (
                            <option value={otherEntity.id} key={otherEntity.id}>
                              {otherEntity.login}
                            </option>
                          ))
                        : null}
                    </AvInput>
                  </AvGroup>
                )}
                <Button id="cancel-save" replace color="info" onClick={this.handleClose}>
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
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
  bloodPressureEntity: storeState.bloodPressure.entity,
  loading: storeState.bloodPressure.loading,
  updating: storeState.bloodPressure.updating,
  updateSuccess: storeState.bloodPressure.updateSuccess,
  isAdmin: hasAnyAuthority(storeState.authentication.account.authorities, [AUTHORITIES.ADMIN])
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
)(BloodPressureUpdate);
