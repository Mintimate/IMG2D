/**
 * JSON对象根据属性排序
 * @param filed 依赖属性
 * @param rev 是否倒序
 * @param primer 类型转换
 * @returns {(function(*, *): (*|number))|*}
 */
export function sortBy (filed, rev, primer) {
  rev = (rev) ? -1 : 1;
  return function (a, b) {
    a = a[filed];
    b = b[filed];
    if (typeof (primer) != 'undefined') {
      a = primer(a);
      b = primer(b);
    }
    if (a < b) { return rev * -1; }
    if (a > b) { return rev * 1; }
    return 1;
  }
}

/**
 * 计算JSON数组的补集
 * @param arr1 需要计算的数组
 * @param arr2 减数
 * @returns {*[]}
 */
export function makeMinus(arr1,arr2) {
  let result = new Array();
  let obj = {};
  for (let i = 0; i < arr2.length; i++) {
    obj[arr2[i]] = 1;
  }
  for (let j = 0; j < arr1.length; j++) {
    if (!obj[arr1[j]])
    {
      obj[arr1[j]] = 1;
      result.push(arr1[j]);
    }
  }
  return result;
}
