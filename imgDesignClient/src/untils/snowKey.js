let CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
let SCALE = 62;

export function productSnowKey(rawString) {
  const strLength = rawString.length
  let strArray = new Array(Math.ceil(strLength / 10))
  let productString = ""
  for (let i = 0; i <= Math.ceil(strLength / 10 - 1); i++) {
    if (i != Math.ceil(strLength / 10 - 1)) {
      strArray[i] = rawString.slice(0 + i * 10, 10 + i * 10)
      productString += string10to62(Number(strArray[i])) + "!"
    } else {
      strArray[i] = rawString.slice(0 + i * 10, strLength)
      productString += string10to62(Number(strArray[i]))
    }
  }
  return productString
}

export function backToSnowKey(productString) {
  let strCom = productString.split("!")
  let Snowkey = ""
  let SnowOne=""
  let SnowTwo=""
  SnowOne=string62to10(strCom[0]).toString()
  SnowTwo=string62to10(strCom[1]).toString()
  while((SnowOne+SnowTwo).length<19){
    SnowOne+="0"
  }
  Snowkey=SnowOne+SnowTwo
  return Snowkey
}

function string10to62(number) {
  number = parseInt(number)
  let string = new Array();
  let remainder
  while (Math.abs(number) > SCALE - 1) {
    remainder = parseInt(number % SCALE);
    string.push(CHARS.charAt(remainder));
    number = parseInt(number / SCALE);
  }
  string.push(CHARS.charAt(number))
  return string.reverse().join("")
}

function string62to10(string) {
  let result = 0;
  let index = 0;
  let length = string.length;
  for (let i = 0; i < length; i++) {
    index = CHARS.indexOf(string.charAt(i));
    result += (index * Math.pow(SCALE, length - i - 1));
  }
  return result;
}
