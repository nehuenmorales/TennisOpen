import React from 'react';

import { ThreeDots } from 'react-loader-spinner'

import s from './TennisSpiner.module.css'


function TennisLoader() {
    return (
        <div class="d-flex align-items-center justify-content-center">
            <ThreeDots
                height="80"
                width="80"
                radius="9"
                color="#4fa94d"
                ariaLabel="three-dots-loading"
                wrapperStyle={{}}
                wrapperClassName=""
                visible={true}
            />
        </div>


    );
}

export default TennisLoader;
