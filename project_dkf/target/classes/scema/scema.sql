DROP DATABASE IF EXISTS dkf;
CREATE DATABASE dkf;
USE dkf;

CREATE TABLE User(
    UserName VARCHAR(20) NOT NULL,
    Password VARCHAR(10) NOT NULL,
    UserEmail VARCHAR(50) NOT NULL ,
    UserContact VARCHAR(12) NOT NULL,
    UserAddress TEXT,
    Position VARCHAR(30),
    CONSTRAINT PRIMARY KEY (UserName)
);

CREATE TABLE LogHistory(
    UserName VARCHAR(20) NOT NULL,
    LogIn DATETIME,
    logOut DATETIME,
    CONSTRAINT FOREIGN KEY(UserName) REFERENCES User(UserName)ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Category(
    CategoryID VARCHAR(6) NOT NULL,
    Des TEXT,
    BasicSalary DOUBLE(11,2),
    CONSTRAINT PRIMARY KEY (CategoryID)
);

CREATE TABLE Employee(
    EmployeeID VARCHAR(6) NOT NULL,
    UserName VARCHAR(20),
    CategoryID VARCHAR(6) NOT NULL,
    EmpFirstName VARCHAR(15),
    EmpLastName VARCHAR(15),
    EmpDob DATE,
    joinDate DATE,
    EmpContact VARCHAR(12),
    EmpAddress TEXT,
    CONSTRAINT PRIMARY KEY (EmployeeID),
    CONSTRAINT FOREIGN KEY(UserName) REFERENCES User(UserName)ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FOREIGN KEY(CategoryID) REFERENCES Category(CategoryID)ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Attendance(
    EmployeeID VARCHAR(6) NOT NULL,
    OtTime DOUBLE(2,2),
    TimeOut TIME,
    TimeIn TIME,
    TtlWorkHours DOUBLE(2,2),
    AP VARCHAR(2),
    Date DATE,
    CONSTRAINT FOREIGN KEY(EmployeeID) REFERENCES Employee(EmployeeID)ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Fund(
    EmployeeID VARCHAR(6) NOT NULL,
    Etf DOUBLE(10,2),
    Epf DOUBLE(10,2),
    Month INT(2),
    Year INT(4),
    CONSTRAINT FOREIGN KEY(EmployeeID) REFERENCES Employee(EmployeeID)ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Salary(
    SalaryID VARCHAR(6)NOT NULL ,
    EmployeeID VARCHAR(6) NOT NULL,
    Transport DOUBLE(10,2),
    Date DATE,
    Attendance DOUBLE(10,2),
    Ot DOUBLE(10,2),
    Skill DOUBLE(10,2),
    Rent DOUBLE(10,2),
    CONSTRAINT PRIMARY KEY (SalaryID),
    CONSTRAINT FOREIGN KEY(EmployeeID) REFERENCES Employee(EmployeeID)ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Buyer(
    BuyerID VARCHAR(6) NOT NULL ,
    BuyerName TEXT NOT NULL ,
    BuyerCN VARCHAR(12) NOT NULL ,
    BuyerAddress TEXT NOT NULL ,
    CONSTRAINT PRIMARY KEY (BuyerID)
);

CREATE TABLE Orders(
    OrderID VARCHAR(6)NOT NULL ,
    BuyerID VARCHAR(6)NOT NULL ,
    Dedline DATE NOT NULL ,
    TtlQty int NOT NULL ,
    DailyOutQty int NOT NULL ,
    PayTerm TEXT NOT NULL ,
    OrderDate DATE NOT NULL ,
    PRIMARY KEY (OrderID),
    CONSTRAINT FOREIGN KEY (BuyerID)REFERENCES Buyer(BuyerID)ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE TrimCard(
    OrderID VARCHAR(6)NOT NULL ,
    TrimID VARCHAR(30),
    type VARCHAR(20),
    Colour VARCHAR(10),
    ReqQty INT,
    CONSTRAINT PRIMARY KEY (TrimID),
    CONSTRAINT FOREIGN KEY (OrderID)REFERENCES Orders(OrderID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE OrderRatio(
    OrderID VARCHAR(6)NOT NULL ,
    ClotheID VARCHAR(30),
    Disc TEXT NOT NULL ,
    Colour VARCHAR(10) NOT NULL ,
    SQty INT (20) NOT NULL ,
    MQty INT(20) NOT NULL ,
    LQty INT(20) NOT NULL ,
    XLQty INT(20) NOT NULL ,
    XXLQty INT(20) NOT NULL ,
    CONSTRAINT PRIMARY KEY (ClotheID),
    FOREIGN KEY (OrderID)REFERENCES Orders(OrderID)ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Input(
    InputID VARCHAR(6)NOT NULL ,
    OrderID VARCHAR(6)NOT NULL ,
    RsdQty INT(30),
    SndQty INT(30),
    Seller VARCHAR(30),
    Date DATE,
    CONSTRAINT PRIMARY KEY (InputID),
    CONSTRAINT FOREIGN KEY (OrderID)REFERENCES Orders(OrderID)ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Cut(
    OrderId VARCHAR(6)NOT NULL ,
    ClotheID VARCHAR(30),
    Date DATE,
    Time TIME,
    CutQty VARCHAR(30),
    Type VARCHAR(40),
    Size VARCHAR(4),
    FOREIGN KEY (OrderId)REFERENCES Orders(OrderID)ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE CutIn(
    InputID VARCHAR(6)NOT NULL ,
    OrderId VARCHAR(6)NOT NULL ,
    CONSTRAINT FOREIGN KEY (OrderId)REFERENCES Cut(OrderId)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (InputID)REFERENCES Input(InputID)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT PRIMARY KEY (InputID,OrderId)
);

CREATE TABLE Material(
    OrderID VARCHAR(6) NOT NULL ,
    MatID VARCHAR(10),
    Time TIME,
    MaterialQty INT(30),
    Date DATE,
    CONSTRAINT FOREIGN KEY  (OrderID)REFERENCES Orders(OrderID)ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE MatIn(
    InputID VARCHAR(6)NOT NULL ,
    OrderID VARCHAR(6) NOT NULL ,
    CONSTRAINT FOREIGN KEY (OrderID)REFERENCES Material(OrderID)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (InputID)REFERENCES Input(InputID)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT PRIMARY KEY (InputID,OrderID)
);

CREATE TABLE Output(
    OutputID VARCHAR(6) NOT NULL ,
    Day DATE,
    Time TIME,
    ClotheId VARCHAR(30),
    size VARCHAR(6),
    DailyOut INT(30),
    CONSTRAINT FOREIGN KEY (OutputID)REFERENCES Orders(OrderID)ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IO(
    OutputID VARCHAR(6) NOT NULL ,
    InputID VARCHAR(6)NOT NULL ,
    CONSTRAINT FOREIGN KEY (OutputID)REFERENCES Output(OutputID)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (InputID)REFERENCES Input(InputID)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT PRIMARY KEY (InputID,OutputID)
);

CREATE TABLE Packing(
    PackID VARCHAR(6)NOT NULL ,
    Date DATE,
    Time TIME,
    ClotheID VARCHAR(30),
    Size VARCHAR(5),
    PackQty INT (30),
    CONSTRAINT FOREIGN KEY (PackID)REFERENCES Orders(OrderID)ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE OutPack(
    OutputID VARCHAR(6) NOT NULL ,
    PackID VARCHAR(6)NOT NULL ,
    CONSTRAINT FOREIGN KEY (OutputID)REFERENCES Output(OutputID)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (PackID)REFERENCES Packing(PackID)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT PRIMARY KEY (PackID,OutputID)
);

CREATE TABLE Shipment(
    OrderID VARCHAR(6) NOT NULL ,
    buyerName VARCHAR(30),
    buyerAdd Text,
    ClotheId VARCHAR(30),
    size VARCHAR(10),
    Qty INT,
    Dates DATE,
    Detail TEXT,
    CONSTRAINT FOREIGN KEY  (OrderID)REFERENCES Orders(OrderID)ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE Stock(
    ClotheID VARCHAR(30),
    OrderId VARCHAR(6),
    size VARCHAR(10),
    qty int,
    CONSTRAINT FOREIGN KEY  (OrderID)REFERENCES Orders(OrderID),
    CONSTRAINT FOREIGN KEY  (ClotheID)REFERENCES OrderRatio(ClotheID)
);

CREATE TABLE PackStock(
    StockID VARCHAR(6)NOT NULL ,
    PackID VARCHAR(6)NOT NULL ,
    CONSTRAINT FOREIGN KEY (StockID)REFERENCES Stock(OrderId)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (PackID)REFERENCES Packing(PackID)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT PRIMARY KEY (PackID,StockID)
);

CREATE TABLE Payment(
    PayID VARCHAR(6)NOT NULL ,
    Method TEXT,
    Prise DOUBLE(20,2),
    Date DATE,
    CONSTRAINT PRIMARY KEY (PayID)
);

CREATE TABLE ShipPay(
    ShipID VARCHAR(6)NOT NULL,
    PayID VARCHAR(6)NOT NULL ,
    CONSTRAINT FOREIGN KEY (ShipID)REFERENCES Shipment(OrderID)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (PayID)REFERENCES Payment(PayID)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT PRIMARY KEY (ShipID,PayID)
);

CREATE TABLE Customer(
    CustomerID VARCHAR(6)NOT NULL ,
    CstName VARCHAR(30),
    CstCN VARCHAR(12),
    CstAddress TEXT,
    CstEmail TEXT,
    CONSTRAINT PRIMARY KEY (CustomerID)
);

CREATE TABLE Invoice (
    InvoiceID VARCHAR(6) NOT NULL ,
    CustomerID VARCHAR(6)NOT NULL ,
    PayMethod VARCHAR(20),
    Date DATE,
    Qty INT (30),
    Product TEXT,
    CONSTRAINT PRIMARY KEY (InvoiceID),
    CONSTRAINT FOREIGN KEY (CustomerID)REFERENCES Customer(CustomerID)ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE StockInvoice(
    InvoiceID VARCHAR(6) NOT NULL ,
    StockID VARCHAR(6)NOT NULL ,
    CONSTRAINT FOREIGN KEY (InvoiceID)REFERENCES Invoice(InvoiceID)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (StockID)REFERENCES Stock(OrderId)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT PRIMARY KEY (StockID,InvoiceID)
);

