import * as React from 'react';

import { Alert, Table } from 'reactstrap';
import { connect } from 'react-redux';
import { IRootState } from 'app/shared/reducers';
import { getUserPointRank } from '../administration.reducer';
export interface IAppProps extends StateProps {
  getUserPointRank(): void;
}

export interface IState {}

class PointRank extends React.Component<IAppProps, IState> {
  componentDidMount() {
    this.props.getUserPointRank();
  }

  public render() {
    const { users } = this.props;
    const tableHtml = users.map(user => {});
    return (
      <div>
        <Alert color="primary"> 周健康排行</Alert>
        <Table responsive striped size="sm">
          <thead>
            <tr>
              <th>用户Id</th>
              <th>用户名</th>
              <th>得分</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user, i) => (
              <tr key={`entity-${i}`}>
                <td>{user.userId}</td>
                <td>{user.userName}</td>
                <td>{user.upoints}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.administration.userRank
});
type StateProps = ReturnType<typeof mapStateToProps>;

export default connect(
  mapStateToProps,
  { getUserPointRank }
)(PointRank);
