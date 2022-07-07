export function backToTop() {
  let timer = setInterval(function () {
    let osTop = document.documentElement.scrollTop || document.body.scrollTop;
    let isSpeed = Math.floor(-osTop / 3);
    document.documentElement.scrollTop = document.body.scrollTop = osTop + isSpeed;
    this.isTop = true;
    if (osTop === 0) {
      clearInterval(timer);
    }
  }, 10)
}
