import axios from 'axios';
import { message } from 'antd';


	
	export function api2(url,data={},type='GET',method='params') {
	  
	  // const data1 = JSON.stringify(data);
	console.log(url)
	  return new Promise((resolve,reject) => {
	    let promise;
	    axios.defaults.headers.post['Content-Type'] = 'application/json';
	    axios.defaults.headers.common['Authorization'] = sessionStorage.getItem('token');
		console.log(data)
	    if(type==='GET') {
	      promise = axios.get(url,{
	        params: data
	      }
	      );
	    }else{
	       console.log(data)
	      if(method === 'payload'){
	        promise = axios.post(url,data)
	      }else{
	        promise = axios({
	          url,
	          data:data,
	          method:'POST'
	        })
	      }
	    }
	  promise.then((request) => {  // 请求成功
	      resolve(request.data);
	    }).catch((error) => {  // 请求失败
	      // message.error('请求失败：'+ error.message);
	      reject(error)
	    })
	  })   
	};