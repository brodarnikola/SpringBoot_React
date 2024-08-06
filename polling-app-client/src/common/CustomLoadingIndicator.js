import React from 'react';
// import { Spin, Icon } from 'antd';

import { Spin } from 'antd';
import { LoadingOutlined } from '@ant-design/icons';

export default function CustomLoadingIndicator(props) {
    const antIcon =  <LoadingOutlined style={{ fontSize: 40 }} spin />;
    return (
        <div style = {{ width: '100%', height: '100%'}}>
            <Spin indicator={antIcon} style = {{display: 'block', textAlign: 'center', marginTop: 40}} />
        </div>
    );
}