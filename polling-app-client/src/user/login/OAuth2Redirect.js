import React, { useEffect, useState } from 'react'
import {Link, useLocation} from 'react-router-dom'
import { parseJwt } from '../../util/Helpers'
import {ACCESS_TOKEN} from "../../constants";

function OAuth2Redirect(props) {
  // const { userLogin } = useContext(AuthContext)
  const [redirectTo, setRedirectTo] = useState('/login')

  const location = useLocation()

  useEffect(() => {
    const accessToken = extractUrlParameter('token')
    if (accessToken) {
      handleLogin(accessToken)
      const redirect = '/'
      setRedirectTo(redirect)
    }
  }, [])

  const extractUrlParameter = (key) => {
    return new URLSearchParams(location.search).get(key)
  }

  const handleLogin = (accessToken) => {
    console.log("first  accessToken is: " + accessToken);

    localStorage.setItem(ACCESS_TOKEN, accessToken);

    const data = parseJwt(accessToken)
    const user = { data, accessToken }

    console.log("handle login .. data is:" + data
        + "user is: " + user + " accessToken is: " + accessToken);

    props.onLogin(true);
    // userLogin(user)
  };

  // return <Link to={redirectTo} />
  return <></>;
}

export default OAuth2Redirect