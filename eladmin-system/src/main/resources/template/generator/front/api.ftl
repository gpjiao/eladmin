import request from '@/utils/request'

export function add(data) {
  return request({
    url: '${moduleName}/${changeClassName}',
    method: 'post',
    data
  })
}

export function del(id) {
  return request({
    url: '${moduleName}/${changeClassName}/' + id,
    method: 'delete'
  })
}

export function edit(data) {
  return request({
    url: '${moduleName}/${changeClassName}',
    method: 'put',
    data
  })
}
