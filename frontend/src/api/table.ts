/* eslint-disable @iceworks/best-practices/no-http-url */
import { api2 } from './ajax';

// 参考之前index.js
export const test = (data) => api2('http://47.104.202.153:8080/api/v1/gluttony/job/pivotChart',data,'POST');  
export const getTable = (data) => api2('http://47.104.202.153:8080/api/v1/gluttony/job/table',data,'POST'); 
export const getTableColumn = (data) => api2('http://47.104.202.153:8080/api/v1/gluttony/metadata/datasource',data,'GET');
export const getOperation = () => api2('http://47.104.202.153:8080/api/v1/gluttony/job/table',{},'GET'); 
export const getAllOperation = () => api2('http://47.104.202.153:8080/api/v1/operator/filter',{},'GET'); 
export const getAllGroup = () => api2('http://47.104.202.153:8080/api/v1/operator/group',{},'GET'); 
