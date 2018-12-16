import React from 'react';
import { Spin, Icon } from 'antd';

export default function CustomLoadingIndicator(props) {
    const antIcon = <Icon type="loading-3-quarters" style={{ fontSize: 40 }} spin />;
    return (
        <div style = {{ width: '100%', height: '100%'}}>
            <Spin indicator={antIcon} style = {{display: 'block', textAlign: 'center', marginTop: 40}} />
        </div>
    );
}