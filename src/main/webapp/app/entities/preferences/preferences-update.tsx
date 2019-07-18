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
import { getEntity, updateEntity, createEntity, reset } from './preferences.reducer';
import { IPreferences } from 'app/shared/model/preferences.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

export interface IPreferencesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPreferencesUpdateState {
  isNew: boolean;
  userId: string;
}

export class PreferencesUpdate extends React.Component<IPreferencesUpdateProps, IPreferencesUpdateState> {
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
      const { preferencesEntity } = this.props;
      const entity = {
        ...preferencesEntity,
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
    const { preferencesEntity, users, loading, updating, isAdmin } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="twentyOnePointsReactApp.preferences.home.createOrEditLabel">
              <Translate contentKey="twentyOnePointsReactApp.preferences.home.createOrEditLabel">Create or edit a Preferences</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : preferencesEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="preferences-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="preferences-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="weeklyGoalLabel" for="preferences-weeklyGoal">
                    <Translate contentKey="twentyOnePointsReactApp.preferences.weeklyGoal">Weekly Goal</Translate>
                  </Label>
                  <AvField
                    id="preferences-weeklyGoal"
                    type="string"
                    className="form-control"
                    name="weeklyGoal"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      min: { value: 10, errorMessage: translate('entity.validation.min', { min: 10 }) },
                      max: { value: 21, errorMessage: translate('entity.validation.max', { max: 21 }) },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="weightUnitsLabel" for="preferences-weightUnits">
                    <Translate contentKey="twentyOnePointsReactApp.preferences.weightUnits">Weight Units</Translate>
                  </Label>
                  <AvInput
                    id="preferences-weightUnits"
                    type="select"
                    className="form-control"
                    name="weightUnits"
                    value={(!isNew && preferencesEntity.weightUnits) || 'KG'}
                  >
                    <option value="KG">{translate('twentyOnePointsReactApp.Units.KG')}</option>
                    <option value="LB">{translate('twentyOnePointsReactApp.Units.LB')}</option>
                  </AvInput>
                </AvGroup>
                {isAdmin && (
                  <AvGroup>
                    <Label for="user.login">User</Label>
                    <AvInput id="preferences-user" type="select" className="form-control" name="userId">
                      <option value="" key="0" />
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
  preferencesEntity: storeState.preferences.entity,
  loading: storeState.preferences.loading,
  updateSuccess: storeState.preferences.updateSuccess,
  updating: storeState.preferences.updating,
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
)(PreferencesUpdate);
