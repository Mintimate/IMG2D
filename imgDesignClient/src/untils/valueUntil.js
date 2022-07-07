/**
 * 判断数据是否为空数据
 * @param obj
 * @returns {boolean}
 */
export function isEmpty(obj) {
  if (typeof obj == "undefined" || obj == null || obj == "") {
    return true;
  } else {
    return false;
  }
}

