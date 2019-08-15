import React, { Component } from 'react';
import {
  Container,
  Row,
  Col,
  Alert,
  Button,
  UncontrolledAlert,
  Badge,
  ButtonDropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem,
  UncontrolledButtonDropdown,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Pagination,
  PaginationItem,
  PaginationLink,
  Progress,
  Spinner,
  UncontrolledTooltip
} from 'reactstrap';

interface IProps {}

interface IState {
  modal: boolean;
}

export class About extends Component<IProps, IState> {
  constructor(props: IProps) {
    super(props);
    this.state = {
      modal: false
    };
    this.toggle = this.toggle.bind(this);
  }

  toggle() {
    this.setState({
      modal: !this.state.modal
    });
  }

  render() {
    return (
      <Container>
        <Row className="mb-3">
          <Col>
            <Spinner color="danger" style={{ width: '3rem', height: '3rem' }} />

            <Spinner color="warning" id="UncontrolledTooltipExample" style={{ width: '3rem', height: '3rem' }} type="grow" />
            <UncontrolledTooltip placement="right" target="UncontrolledTooltipExample">
              Hello world!
            </UncontrolledTooltip>
          </Col>
        </Row>
        <Row className="mb-3">
          <Col>
            <div className="text-center">463 of 500进度条上面的内容</div>
            <Progress color="warning" value={50} max="135" />
            <Progress value="463" max={500}>
              45464464
            </Progress>
            <Progress color="success" value="35">
              Wow!进度条中显示内容
            </Progress>
            <div className="text-center">With Labels</div>
            <Progress multi>
              {/* 多个内容在一个进度条中显示，分为多段，效果像断点续传 */}
              <Progress bar value="15">
                Meh 关键字 bar
              </Progress>
              <Progress striped bar color="success" value="35">
                Wow!
              </Progress>
              <Progress bar animated color="warning" value="25">
                25%
              </Progress>
              <Progress bar color="danger" value="25">
                LOOK OUT!!
              </Progress>
            </Progress>
          </Col>
        </Row>
        <Row className="mb-3">
          <Pagination size="sm" aria-label="Page navigation example" a>
            <PaginationItem>
              <PaginationLink first href="#">
                首页
              </PaginationLink>
            </PaginationItem>
            <PaginationItem>
              <PaginationLink previous href="#" />
            </PaginationItem>
            <PaginationItem>
              <PaginationLink href="#">1</PaginationLink>
            </PaginationItem>
          </Pagination>
        </Row>
        <Row className="mb-3">
          <Col>
            <div>
              <Button color="danger" onClick={this.toggle}>
                模态窗口
              </Button>
              <Modal isOpen={this.state.modal} toggle={this.toggle} className="success">
                <ModalHeader toggle={this.toggle} charCode="Y">
                  Modal title
                </ModalHeader>
                <ModalHeader>Modal title</ModalHeader>
                <ModalHeader toggle={this.toggle}>Modal title</ModalHeader>
                <ModalBody>Lorem ipsum dolor sit amet</ModalBody>
                <ModalFooter>
                  <Button color="primary" onClick={this.toggle}>
                    Do Something
                  </Button>{' '}
                  <Button color="secondary" onClick={this.toggle}>
                    Cancel
                  </Button>
                </ModalFooter>
              </Modal>
            </div>
          </Col>
        </Row>
        <Row className="mb-3">
          <Col className="">
            <Button color="primary" size="lg">
              Large Button
            </Button>{' '}
            <Button color="secondary" size="lg">
              Large Button
            </Button>
            <Badge href="#" color="primary" pill>
              primary
            </Badge>
            <Badge color="primary" pill>
              Primary2
            </Badge>
            <Button outline color="danger">
              outline danger
            </Button>
            <Button inline color="warning">
              {' '}
              inline warning
            </Button>
            <Button outside color="info">
              outside info
            </Button>
            <Button color="info">info</Button>
            {/* <Alert color="success" isOpen={true}>alert</Alert> */}
            <UncontrolledAlert color="dark">UncontrolledAlert</UncontrolledAlert>
            <ButtonDropdown isOpen={!this.toggle} toggle={this.toggle}>
              <DropdownToggle caret color="primary">
                Text
              </DropdownToggle>
              <DropdownMenu>
                <DropdownItem header>Header</DropdownItem>
                <DropdownItem disabled>Action</DropdownItem>
                <DropdownItem>Another Action</DropdownItem>
                <DropdownItem divider />
                <DropdownItem>Another Action</DropdownItem>
              </DropdownMenu>
            </ButtonDropdown>
            <UncontrolledButtonDropdown direction="up">
              <DropdownToggle caret>UncontrolledButtonDropdown</DropdownToggle>
              <DropdownMenu>
                <DropdownItem header>Header</DropdownItem>
                <DropdownItem disabled>Action</DropdownItem>
                <DropdownItem>Another Action</DropdownItem>
                <DropdownItem divider />
                <DropdownItem>Another Action</DropdownItem>
              </DropdownMenu>
            </UncontrolledButtonDropdown>
          </Col>
        </Row>
        <Row className="mb-3">
          <Col md="8">
            <div className="mt-2">
              <p className="p-1 text-right border border-primary">ddd</p>
              <p className="p-1 align-content-bottom">ddd</p>
              <p className="p-3 align-content-top">ddd</p>
              <p className="p-3">ddd</p>
              <p className="p-3">lor</p>
            </div>
          </Col>
          <Col>
            <div className="mt-2">
              <p className="p-3">Lorem ipsum dolor sit.</p>
            </div>
          </Col>
        </Row>
        <Row className="mb-3">
          <Col md="5" xs="3" flex>
            <Alert order={8} color="primary">
              {' '}
              <a className="alert-link"> Alert color="primary" </a>
            </Alert>
            <Alert order={7} color="secondary">
              {' '}
              Alert color="secondary"{' '}
            </Alert>
            <Alert order={6} color="success">
              {' '}
              Alert color="success"{' '}
            </Alert>
            <Alert order={5} color="danger">
              Alert color="danger"{' '}
            </Alert>
            <Alert order={4} color="warning">
              {' '}
              Alert color="warning"{' '}
            </Alert>
            <Alert order={3} color="info">
              {' '}
              Alert color="info"{' '}
            </Alert>
            <Alert order={2} color="light">
              {' '}
              Alert color="danger"{' '}
            </Alert>
            <Alert order={1} color="dark">
              Alert color="danger"{' '}
            </Alert>
          </Col>
          <Col md="4" xs="1">
            <Alert color="warning">Col md="4" Alert color="warning" </Alert>
          </Col>
        </Row>
        <Row className="mb-3">
          <Col md="8" xs="4">
            <Alert color="primary">
              {' '}
              Col md="8" <a className="alert-link"> Alert color="primary" </a>
            </Alert>
            <Alert color="secondary">Col md="8" Alert color="secondary" </Alert>
            <Alert color="success">Col md="8" Alert color="success" </Alert>
            <Alert color="danger">Col md="8" Alert color="danger" </Alert>
            <Alert color="warning">Col md="8" Alert color="warning" </Alert>
            <Alert color="info">Col md="8" Alert color="info" </Alert>
            <Alert color="light">Col md="8" Alert color="danger" </Alert>
            <Alert color="dark">Col md="8" Alert color="danger" </Alert>
          </Col>
          <Col md="4" xs="1">
            <Alert color="warning">Col md="4" Alert color="warning" </Alert>
          </Col>
        </Row>
      </Container>
    );
  }
}

export default About;
