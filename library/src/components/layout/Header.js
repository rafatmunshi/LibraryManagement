import React from 'react';

const Header = () => {
    return ( <header style = { headerStyle } >
        <h1> Library Management System </h1>
        View Books in the Library </header>
    )
};

const headerStyle = {
    background: "#333",
    color: '#fff',
    textAlign: 'center',
    padding: '10px'
};

export default Header;